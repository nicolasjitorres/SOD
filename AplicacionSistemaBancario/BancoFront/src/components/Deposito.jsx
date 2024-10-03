import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Deposito() {
  const [monto, setMonto] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    navigate("/");
  }

  const handleClick = () => {
    navigate("/home");
  }

  const handleDeposito = async (e) => {
    e.preventDefault();
    try {
      console.log(accountId + monto);

      await axios.post('http://localhost:8080/cuentas/depositar', {
        idCuenta: parseInt(accountId),
        monto: parseFloat(monto)
      });
      alert('Depósito realizado con éxito');
    } catch (err) {
      setError('Error al realizar el depósito' + err.message);
    }
  };

  return (
    <div>
      <h2>Depositar Dinero</h2>
      <form onSubmit={handleDeposito}>
        <input
          type="number"
          placeholder="Monto"
          value={monto}
          onChange={(e) => setMonto(e.target.value)}
          required
        />
        <button type="submit">Depositar</button>
      </form>

      <br />
      <br />
      <div>
        <button onClick={handleClick}>Volver al home</button>
      </div>
      {error && <p>{error}</p>}
    </div>
  );
}

export default Deposito;
