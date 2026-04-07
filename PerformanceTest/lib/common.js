import http from 'k6/http';
import { check } from 'k6';

export const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export function authHeaders() {
  const token = __ENV.AUTH_BEARER_TOKEN;
  if (token) {
    return {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    };
  }
  return {
    'Content-Type': 'application/json',
  };
}

export function parseJsonOrDefault(value, fallback) {
  try {
    return value ? JSON.parse(value) : fallback;
  } catch (e) {
    return fallback;
  }
}

export function get(urlPath, params = {}) {
  return http.get(`${BASE_URL}${urlPath}`, params);
}

export function post(urlPath, payload, params = {}) {
  return http.post(`${BASE_URL}${urlPath}`, JSON.stringify(payload), params);
}

export function check200(res, name = 'status 200') {
  return check(res, {
    [name]: (r) => r.status === 200,
  });
}
