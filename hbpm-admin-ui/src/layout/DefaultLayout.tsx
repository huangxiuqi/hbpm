import React, { useContext } from 'react';
import { Breadcrumb, Layout, Menu } from 'antd';
import './DefaultLayout.less';
import { Outlet, useLocation } from 'react-router-dom';
import { menuContext } from '@/App';
import { findAllByPath, findAncestorByPath, findMenuByPath, transform } from '@/utils/menu-utils';

const { Header, Sider } = Layout;

const DefaultLayout: React.FC = () => {
  const location = useLocation();
  const menu = useContext(menuContext);

  const transformMenu = transform(menu);
  const selected = findMenuByPath(menu, location.pathname).map(i => i.id!);
  const parents = findAncestorByPath(menu, location.pathname).map(i => i.id!);
  const all = findAllByPath(menu, location.pathname);

  return (
    <Layout className="page-layout">
      <Header className="page-header">
        <div className="logo"/>
      </Header>
      <div className="page-header-placeholder"></div>
      <Layout hasSider className="main-layout">
        <Sider width={250} className="menu-side">
          {
            menu?.length > 0
              ? <Menu
                mode="inline"
                style={{ height: '100%', borderRight: 0 }}
                items={transformMenu}
                selectedKeys={selected}
                defaultOpenKeys={parents}
              />
              : null
          }
        </Sider>
        <Layout>
          <div className="page-bread-crumb">
            <Breadcrumb>
              {
                all.reverse().map(it => {
                  return <Breadcrumb.Item key={it.id}>{it.name}</Breadcrumb.Item>;
                })
              }
            </Breadcrumb>
          </div>
          <Layout className="content-layout">
            <Outlet/>
          </Layout>
        </Layout>
      </Layout>
    </Layout>
  );
};

export default DefaultLayout;
