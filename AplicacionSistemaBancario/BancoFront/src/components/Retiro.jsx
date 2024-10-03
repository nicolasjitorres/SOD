import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Retiro() {
  const [monto, setMonto] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    navigate("/");
  }

  const handleClick = () => {
    navigate("/home");
  }

  const handleRetiro = async (e) => {
    e.preventDefault();
    setLoading(true); 
    try {
      await axios.post('http://localhost:8080/cuentas/retirar', {
        idCuenta: accountId,
        monto: parseFloat(monto)
      });
      alert('Retiro realizado con éxito');
    } catch (err) {
      setError('Error al realizar el retiro: ' + err.message);
    } finally {
      setLoading(false); 
    }
  };

  return (
    <div>
      <h2>Retirar Dinero</h2>
      <form onSubmit={handleRetiro}>
        <input
          type="number"
          placeholder="Monto"
          value={monto}
          onChange={(e) => setMonto(e.target.value)}
          required
        />
        <button type="submit">Retirar</button>
      </form>
      <br />
      <br />
      <div>
        <button onClick={handleClick}>Volver al home</button>
      </div>
      {loading && <p>Realizando retiro... Por favor espere hasta tener saldo suficiente...</p>}
      {error && <p>{error}</p>}
    </div>
  );
}

export default Retiro;
