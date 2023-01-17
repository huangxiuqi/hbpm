import React, {useState, useEffect, useCallback} from "react";
import {Form, Input, Button, Spin} from 'antd';
import {UserOutlined, LockOutlined, SafetyCertificateOutlined, LoadingOutlined} from '@ant-design/icons';
import './PasswordLogin.less';
import {loginPageVars} from '../App';
import {login, getCaptcha} from '../services/login';

interface Captcha {
  captchaId: string,
  captchaImage: string,
  captchaValue: string,
}

const PasswordLogin: React.FC = () => {

  const [captcha, setCaptcha] = useState<Captcha>({
    captchaId: '',
    captchaImage: '',
    captchaValue: '',
  });

  const [isSubmit, setSubmit] = useState<boolean>(false);

  const [isCaptchaLoading, setCaptchaLoading] = useState(true);

  const [form] = Form.useForm();

  const onFinish = (values: any) => {
    setSubmit(true);
    login({
      username: values.username,
      password: values.password,
      captchaId: captcha.captchaId,
      captchaValue: values.captcha,
    })
      .then(() => {
        window.location.reload();
      })
      .catch(err => {
        setSubmit(false);
        loadCaptcha(false);
      });
  };

  const loadCaptcha = useCallback((isLoading: boolean) => {
    if (isLoading) {
      return;
    }
    setCaptchaLoading(true);
    let timestamp = new Date().getTime();
    form.setFieldsValue({captcha: ''});
    getCaptcha()
      .then((data: any) => {
        setCaptcha(data as Captcha);
        setCaptchaLoading(false);
      });
  }, [form])

  useEffect(() => {
    loadCaptcha(false);
  }, [loadCaptcha]);

  const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>;

  return (
    <loginPageVars.Consumer>
      {loginPageVars => (
        <React.Fragment>
          <div className="app-content">
            <div className="app-info">
              <div className="app-icon">
                <img src={loginPageVars.clientInfo.icon} alt=""/>
              </div>
              <div className="login-title">登录</div>
              <div className="login-desc">继续使用{loginPageVars.clientInfo.name}</div>
            </div>
            <div className="login-split-25"></div>
            <Form
              name="normal_login"
              className="login-form"
              onFinish={onFinish}
              form={form}
            >
              <Form.Item
                name="username"
                rules={[{required: true, message: '请输入用户名!'}]}
              >
                <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="用户名" size="large"/>
              </Form.Item>
              <Form.Item
                name="password"
                rules={[{required: true, message: '请输入密码!'}]}
              >
                <Input.Password
                  prefix={<LockOutlined className="site-form-item-icon"/>}
                  type="password"
                  placeholder="密码"
                  size="large"
                  allowClear
                />
              </Form.Item>
              <Form.Item>
                <div className="captcha-item">
                  <div className="captcha-input">
                    <Form.Item
                      name="captcha"
                      noStyle
                      rules={[{required: true, message: '请输入验证码!'}]}
                    >
                      <Input
                        prefix={<SafetyCertificateOutlined className="site-form-item-icon"/>}
                        placeholder="验证码"
                        size="large"
                        allowClear
                      />
                    </Form.Item>
                  </div>
                  <div className="captcha-img" onClick={() => loadCaptcha(isCaptchaLoading)}>
                    {
                      isCaptchaLoading ?
                        <Spin indicator={antIcon}/> :
                        <img src={captcha.captchaImage} alt="验证码图片" title="点击更换验证码图片"/>
                    }
                  </div>
                </div>
              </Form.Item>
              <Form.Item>
                <Button type="primary"
                        size="large"
                        block
                        loading={isSubmit}
                        htmlType="submit"
                        className="login-form-button">
                  登录
                </Button>
              </Form.Item>
            </Form>
          </div>
          <div className="module-footer"></div>
        </React.Fragment>
      )}
    </loginPageVars.Consumer>
  );
}

export default PasswordLogin;
