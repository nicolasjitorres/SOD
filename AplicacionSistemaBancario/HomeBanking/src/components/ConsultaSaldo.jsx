import { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function ConsultaSaldo() {
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");
  const [saldo, setSaldo] = useState(null);
  const [nombre, setNombre] = useState('');
  const [error, setError] = useState('');

  if (!accountId) {
    navigate("/");
  }

  const obtenerDetallesCuenta = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/cuentas/${accountId}`);
      setSaldo(response.data.saldo);
      setNombre(response.data.cliente.nombre); 
    } catch (err) {
      setError('Error al obtener los detalles de la cuenta: ' + err.message);
    }
  };

  useEffect(() => {
    obtenerDetallesCuenta();
  }, [accountId]);

  return (
    <div>
      <h2>Consulta de Saldo</h2>
      {error && <p>{error}</p>}
      {nombre && <h3>Nombre del Usuario: {nombre}</h3>}
      {saldo !== null && <h3>Saldo Actual: ${saldo}</h3>}
      <button onClick={() => navigate("/home")}>Volver al Inicio</button>
    </div>
  );
}

export default ConsultaSaldo;
