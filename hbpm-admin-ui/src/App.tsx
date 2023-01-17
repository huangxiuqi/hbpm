import React, { createContext, useEffect, useState } from 'react';
import './App.less';
import { RouterProvider } from 'react-router-dom';
import { router } from './router';
import { getMenu } from './services';
import { ConfigProvider, Spin } from 'antd';
import RouteLoadProgressEvent, { PageLoadFinish, PageLoadStart } from '@/events/page-load-event';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

// 关闭Spin
NProgress.configure({ showSpinner: false });
export const menuContext = createContext<API.Menu[]>([]);
const pageLoadStart = () => {
  NProgress.start();
};
const pageLoadFinish = () => {
  NProgress.done();
};

const App: React.FC = () => {
  const [menu, setMenu] = useState<API.Menu[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    RouteLoadProgressEvent.on(PageLoadStart, pageLoadStart);
    RouteLoadProgressEvent.on(PageLoadFinish, pageLoadFinish);
    return () => {
      RouteLoadProgressEvent.off(PageLoadStart, pageLoadStart);
      RouteLoadProgressEvent.off(PageLoadFinish, pageLoadFinish);
    };
  }, []);

  useEffect(() => {
    setLoading(true);
    getMenu()
      .then(menu => {
        setMenu(menu);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <ConfigProvider
      theme={{
        token: {
          borderRadius: 4,
        },
      }}
    >
      {
        loading
          ? <div className="page-loading">
            <Spin size="large"/>
          </div>
          : <menuContext.Provider value={menu}>
            <RouterProvider router={router}/>
          </menuContext.Provider>
      }
    </ConfigProvider>
  );
};

export default App;
