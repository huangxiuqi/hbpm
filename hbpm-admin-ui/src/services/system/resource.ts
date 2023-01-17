import request from '@/utils/request';
import React from 'react';

/**
 * 获取资源树形结构
 */
export async function getResourceTree(params?: {
  name?: string
}) {
  let url = '/api/resource/tree';
  if (params?.name) {
    url = `${url}?name=${params.name}`;
  }
  return request.get<API.Resource[]>(url);
}

/**
 * 获取资源详情
 * @param id
 */
export async function getResource(id: string) {
  return request.get<API.Resource>(`/api/resource/${id}`);
}

/**
 * 保存资源实体
 * @param data
 */
export async function saveResource(data: API.Resource) {
  return request.post('/api/resource', data);
}

/**
 * 删除资源实体
 * @param id
 */
export async function deleteResource(id: string) {
  return request.delete(`/api/resource/${id}`);
}

/**
 * 批量删除资源实体
 * @param ids
 */
export async function batchDeleteResource(ids: React.Key[]) {
  return request.delete('/api/resource/batch', {
    data: {
      ids,
    },
  });
}
