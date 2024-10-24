import { useEffect, useState } from 'react';
import { obtenerMovimientosDeCuenta } from '../services/api';

const MovimientosList = ({ idCuenta }) => {
  const [movimientos, setMovimientos] = useState([]);

  useEffect(() => {
    const fetchMovimientos = async () => {
      try {
        const data = await obtenerMovimientosDeCuenta(idCuenta);
        setMovimientos(data);
      } catch (error) {
        console.error('Error al cargar los movimientos', error);
      }
    };
    fetchMovimientos();
  }, [idCuenta]);

  return (
    <div>
      <h2>Movimientos de la Cuenta {idCuenta}</h2>
      <ul>
        {movimientos.map(movimiento => (
          <li key={movimiento.id}>
            {movimiento.descripcion}: {movimiento.monto}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MovimientosList;
