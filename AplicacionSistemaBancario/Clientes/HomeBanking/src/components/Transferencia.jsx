import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Transferencia() {
  const [monto, setMonto] = useState('');
  const [destinoId, setDestinoId] = useState('');
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

  const handleTransferencia = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await axios.post('http://localhost:8080/cuentas/transferir', {
        idCuenta: accountId,
        idDestino: parseInt(destinoId),
        monto: parseFloat(monto)
      });
      alert('Transferencia realizada con éxito');
    } catch (err) {
      setError('Error al realizar la transferencia: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2>Transferir Dinero</h2>
      <form onSubmit={handleTransferencia}>
        <input
          type="text"
          placeholder="ID de cuenta destino"
          value={destinoId}
          onChange={(e) => setDestinoId(e.target.value)}
          required
        />
        <input
          type="number"
          placeholder="Monto"
          value={monto}
          onChange={(e) => setMonto(e.target.value)}
          required
        />
        <button type="submit">Transferir</button>
      </form>
      <br />
      <br />
      <div>
        <button onClick={handleClick}>Volver al home</button>
      </div>
      {loading && <p>Realizando transferencia... Por favor espere hasta tener saldo suficiente...</p>}
      {error && <p>{error}</p>}
    </div>
  );
}

export default Transferencia;
