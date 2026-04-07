import { sleep, check } from 'k6';
import { authHeaders, parseJsonOrDefault, post } from '../lib/common.js';

const payload = parseJsonOrDefault(__ENV.OUTBOUND_STATUS_PAYLOAD, {
  campaignDbid: 101,
});

export const options = {
  scenarios: {
    spike_outbound_status: {
      executor: 'ramping-arrival-rate',
      startRate: 5,
      timeUnit: '1s',
      preAllocatedVUs: 50,
      maxVUs: 300,
      stages: [
        { duration: '1m', target: 10 },
        { duration: '30s', target: 120 },
        { duration: '2m', target: 120 },
        { duration: '30s', target: 10 },
      ],
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<1500'],
    checks: ['rate>0.95'],
  },
};

export default function () {
  const res = post('/api/v1/outbound/campaigns/status', payload, { headers: authHeaders() });

  check(res, {
    'status endpoint reachable': (r) => [200, 400, 401, 403, 503].includes(r.status),
  });

  sleep(0.2);
}
