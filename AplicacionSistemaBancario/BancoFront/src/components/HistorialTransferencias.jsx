import { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function HistorialTransferencias() {
  const [movimientos, setMovimientos] = useState([]);
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    navigate("/");
  }

  const handleClick = () => {
    navigate("/");
  }

  useEffect(() => {
    const fetchMovimientos = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/cuentas/${accountId}/movimientos`);
        setMovimientos(response.data);
      } catch (error) {
        console.error('Error al obtener movimientos:', error);
      }
    };

    fetchMovimientos();
  }, [accountId]);

  return (
    <div>
      <h2>¡Bienvenido! Este es su historial de Transferencias.</h2>
      <h3>Numero de cuenta: {accountId}</h3>
      <ul>
        {movimientos.map((movimiento) => (
          <li key={movimiento.id}>
            {movimiento.descripcion}: ${movimiento.monto}
          </li>
        ))}
      </ul>
      <button onClick={handleClick}>Volver al Home</button>
    </div>
  );
}

export default HistorialTransferencias;
