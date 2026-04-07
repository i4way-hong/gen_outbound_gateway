import { sleep, check } from 'k6';
import { BASE_URL, authHeaders } from '../lib/common.js';
import http from 'k6/http';

const streamPath = __ENV.SCS_STREAM_PATH || '/api/v1/scs/app-status/stream';

export const options = {
  scenarios: {
    soak_sse: {
      executor: 'constant-vus',
      vus: 20,
      duration: '15m',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.03'],
    checks: ['rate>0.97'],
  },
};

export default function () {
  const res = http.get(`${BASE_URL}${streamPath}`, {
    headers: authHeaders(),
    timeout: '60s',
  });

  check(res, {
    'sse status 200 or 401/403': (r) => [200, 401, 403].includes(r.status),
  });

  sleep(1);
}
