import React from 'react';
import { createHashRouter } from 'react-router-dom';
import DefaultLayout from '@/layout/DefaultLayout';
import ErrorPage from '@/pages/ErrorPage';
import lazy from '@/utils/lazy';

export const router = createHashRouter([
  {
    path: '/',
    element: <DefaultLayout/>,
    errorElement: <ErrorPage/>,
    children: [
      {
        path: '/',
        element: lazy(async () => import('@/pages/Home').then(m => m.default)),
      },
      {
        path: '/system/resource',
        element: lazy(async () => import('@/pages/System/Resource/List').then(m => m.default)),
      },
      {
        path: '/system/role',
        element: lazy(async () => import('@/pages/System/Role/List').then(m => m.default)),
      },
      {
        path: '/system/department',
        element: lazy(async () => import('@/pages/System/Department/List').then(m => m.default)),
      },
      {
        path: '/gateway/router',
        element: lazy(async () => import('@/pages/Gateway/Router/List').then(m => m.default)),
      },
    ],
  },
]);
