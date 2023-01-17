import request from '@/utils/request';
import React from 'react';

/**
 * 获取路由定义详情
 * @param id
 */
export async function getRoute(id: string) {
  return request.get<API.GatewayRouteDefine>(`/api/gateway/route/${id}`);
}

/**
 * 获取路由定义分页
 */
export async function getRoutePage(params: API.PageReq) {
  params.page = params.page ?? 1;
  params.size = params.size ?? 20;
  return request.post<API.Page<API.GatewayRouteDefine>>('/api/gateway/route/page', params);
}

/**
 * 保存路由定义
 * @param data
 */
export async function saveRoute(data: API.GatewayRouteDefine) {
  return request.post('/api/gateway/route', data);
}

/**
 * 删除路由定义
 * @param id
 */
export async function deleteRoute(id: string) {
  return request.delete(`/api/gateway/route/${id}`);
}

/**
 * 批量删除路由定义
 * @param ids
 */
export async function batchDeleteRoute(ids: React.Key[]) {
  return request.delete('/api/gateway/route/batch', {
    data: {
      ids,
    },
  });
}
