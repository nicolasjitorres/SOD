import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { iniciarSesion } from '../../../Posnet/src/services/api';

function Login() {
  const [accountId, setAccountId] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const idLocal = localStorage.getItem("accountId");


  if (!accountId) {
    navigate("/");
  }

  useEffect(() => {
    if (idLocal) {
      navigate("/home");
    }
  });

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await iniciarSesion(accountId, password);
      console.log(response);
      
      if (response) {
        localStorage.setItem("accountId", response.accountId);
        localStorage.setItem("saldo", response.saldo);
        navigate('/home'); 
      }
    } catch (err) {
      setError('Error: ' + err.message);
    }
  };

  return (
    <div>
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleLogin}>
        <input
          type="number"
          placeholder="Número de cuenta"
          value={accountId}
          onChange={(e) => setAccountId(e.target.value)}
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Iniciar Sesión</button>
      </form>
      {error && <p>{error}</p>}
    </div>
  );
}

export default Login;
