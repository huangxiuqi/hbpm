import axios, { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';
import { message } from 'antd';
import Cookies from 'js-cookie';

declare class Request {
  request<T = any, D = any>(config: AxiosRequestConfig<D>): Promise<T>;

  get<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;

  delete<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;

  head<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;

  options<T = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<T>;

  post<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;

  put<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;

  patch<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;

  postForm<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;

  putForm<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;

  patchForm<T = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<T>;
}

const instance = axios.create();

const onRequest = <D>(config: AxiosRequestConfig<D>): AxiosRequestConfig<D> => {
  const csrfToken = Cookies.get('gateway_csrf_token');
  config.headers = {
    'X-Requested-With': 'XMLHttpRequest',
    'X-XSRF-TOKEN': csrfToken,
  };
  if (!config.timeout) {
    config.timeout = 3000;
  }
  config.withCredentials = true;
  return config;
};

const onRequestError = async <T, D>(error: AxiosError<T, D>): Promise<AxiosError<T, D>> => {
  console.log('请求错误', error);
  return Promise.reject(error);
};

const onResponse = <T, D>(response: AxiosResponse<API.BaseResponse<T>, D>): T => {
  return response.data.data;
};

const onResponseError = async <T, D>(error: AxiosError<API.BaseResponse<T>, D>): Promise<T> => {
  if (error.response?.status === 401) {
    window.location.reload();
    throw new Error('Unauthorized');
  }
  let msg = error.response?.data?.message;
  if (!msg) {
    msg = '未知错误';
  }
  void message.error(msg).then();
  return Promise.reject(error.response?.data);
};

// 请求拦截器
instance.interceptors.request.use(onRequest, onRequestError);

// 响应拦截器
instance.interceptors.response.use(onResponse, onResponseError);

export default instance as Request;
