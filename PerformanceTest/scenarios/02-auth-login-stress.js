import { sleep, check } from 'k6';
import { BASE_URL, authHeaders, post } from '../lib/common.js';

const username = __ENV.AUTH_LOGIN_USERNAME || 'admin';
const password = __ENV.AUTH_LOGIN_PASSWORD || 'admin123';

export const options = {
  scenarios: {
    stress_login: {
      executor: 'ramping-vus',
      startVUs: 10,
      stages: [
        { duration: '1m', target: 30 },
        { duration: '2m', target: 80 },
        { duration: '2m', target: 120 },
        { duration: '1m', target: 0 },
      ],
      gracefulRampDown: '30s',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.03'],
    http_req_duration: ['p(95)<1200'],
    checks: ['rate>0.97'],
  },
};

export default function () {
  const payload = { username, password };
  const res = post('/auth/login', payload, { headers: authHeaders() });

  check(res, {
    'login status is 200 or 401': (r) => r.status === 200 || r.status === 401,
  });

  sleep(0.5);
}
