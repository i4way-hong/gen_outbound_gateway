import { sleep } from 'k6';
import { get, check200 } from '../lib/common.js';

export const options = {
  scenarios: {
    load_health: {
      executor: 'ramping-vus',
      startVUs: 5,
      stages: [
        { duration: '1m', target: 20 },
        { duration: '3m', target: 20 },
        { duration: '1m', target: 0 },
      ],
      gracefulRampDown: '30s',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    checks: ['rate>0.99'],
  },
};

export default function () {
  const res = get('/actuator/health');
  check200(res, 'health is 200');
  sleep(1);
}
