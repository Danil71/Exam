import { observer } from "mobx-react-lite";
import { useContext, useEffect, useState } from 'react';
import { Button, Form, Modal } from 'react-bootstrap';
import { createPortal } from 'react-dom';
import toast from 'react-hot-toast';
import StoreContext from '../users/StoreContext';
import './otpVerification.css';

const OtpVerificationModal = observer(({ show }) => {
  const [otp, setOtp] = useState('');
  const [resendDisabled, setResendDisabled] = useState(true);
  const [countdown, setCountdown] = useState(90);
  const { store } = useContext(StoreContext);

  const RESEND_TIMER_SECONDS = 5 * 60;

  useEffect(() => {
    let timer;
    if (resendDisabled && countdown > 0) {
      timer = setInterval(() => {
        setCountdown((prevCountdown) => prevCountdown - 1);
      }, 1000);
    } else if (countdown === 0 && resendDisabled) {
      setResendDisabled(false);
    }
    return () => clearInterval(timer);
  }, [resendDisabled, countdown]);

  useEffect(() => {
    if (show) {
      setOtp('');
      setResendDisabled(true);
      setCountdown(90);
    }
  }, [show]);

  const handleChange = (e) => {
    const value = e.target.value;
    if (/^\d*$/.test(value) && value.length <= 6) {
      setOtp(value);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    event.stopPropagation();

    if (otp.length !== 6) {
      toast.error('Пожалуйста, введите полный 6-значный одноразовый пароль.');
      return;
    }

    await store.verifyOtp(store.userEmail, otp);
  };

  const handleResendOtp = async () => {
    if (resendDisabled) return;

    await store.sendOtp(store.userEmail);
    setResendDisabled(true);
    setCountdown(RESEND_TIMER_SECONDS);
  };

  const handleGoBack = async () => {
    if (store.userEmail) {
      await store.invalidateOtp(store.userEmail);
    }
    store.resetAuthState();
  };

  const formatCountdown = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${String(remainingSeconds).padStart(2, '0')}`;
  };

  return createPortal(
    <Modal
      show={show}
      backdrop="static"
      keyboard={false}
      centered
      contentClassName="border-0 shadow-lg rounded-4"
    >
      <Modal.Body className="p-4">
        <div className="text-center mb-4">
          <h3 className="text-dark fw-bolder fs-4 mb-2">Двухфакторная аутентификация</h3>
          <div className="fw-normal text-muted mb-3">
            Введите код верификации, который мы отправили на
          </div>
          <div className="d-flex align-items-center justify-content-center fw-bold mb-4">
            {store.userEmail ? (
              <span>{store.userEmail.replace(/^(.)(.*)(@.*)$/, (_match, p1, _p2, p3) => p1 + '*****' + p3)}</span>
            ) : (
              <span>Неизвестный адрес</span>
            )}
          </div>
        </div>

        <Form noValidate onSubmit={handleSubmit}>
          <Form.Group controlId="otp" className="mb-4">
            <Form.Label className="text-center w-100 text-muted small">
              Введите ваш 6-значный защитный код
            </Form.Label>

            <Form.Control
              type="text"
              inputMode="numeric"
              autoComplete="one-time-code"
              value={otp}
              onChange={handleChange}
              placeholder="------"
              className="form-control text-center fw-bold fs-2 border-2 rounded-3"
              style={{ letterSpacing: '0.5em', maxWidth: '300px', margin: '0 auto' }}
              data-testid="otp-input"
              autoFocus
            />
          </Form.Group>

          <div className="d-grid">
            <Button 
              type="submit" 
              variant="dark" 
              size="lg" 
              className="submit_btn mb-4 rounded-3"
              data-testid="otp-submit-btn"
            >
              Подтвердить
            </Button>
          </div>

          <div className="fw-normal text-muted mb-2 text-center">
            Не получили код?{' '}
            <Button
              variant="link"
              className="text-primary fw-bold text-decoration-none p-0 align-baseline resend-text-hover"
              onClick={handleResendOtp}
              disabled={resendDisabled}
              data-testid="otp-resend-btn"
            >
              Отправить повторно
            </Button>
            {resendDisabled && (
              <span className="ms-2">({formatCountdown(countdown)})</span>
            )}
          </div>
          <div className="text-center mt-3">
            <Button
              variant="link"
              className="text-muted fw-bold text-decoration-none p-0 align-baseline"
              onClick={handleGoBack}
              data-testid="otp-back-btn"
            >
              Назад к логину
            </Button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>,
    document.body
  );
});

export default OtpVerificationModal;