import { useEffect, useState } from 'react';
import { obtenerCuentas, eliminarCuenta } from '../services/api';
import { Link } from 'react-router-dom';

const CuentasList = () => {
  const [cuentas, setCuentas] = useState([]);

  useEffect(() => {
    const fetchCuentas = async () => {
      try {
        const data = await obtenerCuentas();
        setCuentas(data);
      } catch (error) {
        console.error('Error al cargar las cuentas', error);
      }
    };
    fetchCuentas();
  }, []);

  const handleEliminarCuenta = async (idCuenta) => {
    try {
      await eliminarCuenta(idCuenta);
      setCuentas(cuentas.filter(cuenta => cuenta.id !== idCuenta));
    } catch (error) {
      console.error('Error al eliminar la cuenta', error);
    }
  };

  return (
    <div>
      <h2>Lista de Cuentas</h2>
      <Link to="/cuentas/crear">Crear nueva cuenta</Link>
      <ul>
        {cuentas.map(cuenta => (
          <li key={cuenta.id}>
            {cuenta.tipoCuenta} - {cuenta.saldo}
            <button onClick={() => handleEliminarCuenta(cuenta.id)}>Eliminar</button>
            <Link to={`/cuentas/${cuenta.id}/editar`}>Editar</Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CuentasList;
