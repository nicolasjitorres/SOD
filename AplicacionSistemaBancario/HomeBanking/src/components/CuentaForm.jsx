import { useState, useEffect } from 'react';
import { crearCuenta, actualizarCuenta, obtenerCuentaPorId } from '../services/api';
import { useNavigate, useParams } from 'react-router-dom';

const CuentaForm = () => {
  const [cuenta, setCuenta] = useState({ tipoCuenta: '', saldo: '' });
  const navigate = useNavigate();
  const { idCuenta } = useParams();

  useEffect(() => {
    if (idCuenta) {
      const fetchCuenta = async () => {
        try {
          const data = await obtenerCuentaPorId(idCuenta);
          setCuenta(data);
        } catch (error) {
          console.error('Error al obtener la cuenta:', error);
        }
      };
      fetchCuenta();
    }
  }, [idCuenta]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCuenta({ ...cuenta, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (idCuenta) {
        await actualizarCuenta(idCuenta, cuenta);
      } else {
        await crearCuenta(cuenta);
      }
      navigate('/cuentas');
    } catch (error) {
      console.error('Error al guardar la cuenta', error);
    }
  };

  return (
    <div>
      <h2>{idCuenta ? 'Editar Cuenta' : 'Crear Cuenta'}</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Tipo de Cuenta:
          <input type="text" name="tipoCuenta" value={cuenta.tipoCuenta} onChange={handleInputChange} required />
        </label>
        <label>
          Saldo:
          <input type="number" name="saldo" value={cuenta.saldo} onChange={handleInputChange} required />
        </label>
        <button type="submit">{idCuenta ? 'Actualizar' : 'Crear'}</button>
      </form>
    </div>
  );
};

export default CuentaForm;
