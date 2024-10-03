import { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

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
      const response = await axios.post('http://localhost:8080/login', {
        accountId,
        password,
      });
      console.log(response.data);
      
      if (response.status === 200) {
        localStorage.setItem("accountId", response.data.accountId);
        localStorage.setItem("saldo", response.data.saldo);
        navigate('/home'); 
      }
    } catch (err) {
      setError('Credenciales inválidas' + err.message);
    }
  };

  return (
    <div>
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleLogin}>
        <input
          type="text"
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
