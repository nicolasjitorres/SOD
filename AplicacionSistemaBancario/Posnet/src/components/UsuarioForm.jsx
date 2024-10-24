import { useState, useEffect } from 'react';
import { crearUsuario, actualizarUsuario, obtenerUsuarioPorId } from '../services/api';
import { useNavigate, useParams } from 'react-router-dom';

const UsuarioForm = () => {
  const [usuario, setUsuario] = useState({ nombre: '', email: '' });
  const navigate = useNavigate();
  const { idUsuario } = useParams();

  useEffect(() => {
    if (idUsuario) {
      const fetchUsuario = async () => {
        try {
          const data = await obtenerUsuarioPorId(idUsuario);
          setUsuario(data);
        } catch (error) {
          console.error('Error al obtener el usuario:', error);
        }
      };
      fetchUsuario();
    }
  }, [idUsuario]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUsuario({ ...usuario, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (idUsuario) {
        await actualizarUsuario(idUsuario, usuario);
      } else {
        await crearUsuario(usuario);
      }
      navigate('/usuarios');
    } catch (error) {
      console.error('Error al guardar el usuario', error);
    }
  };

  return (
    <div>
      <h2>{idUsuario ? 'Editar Usuario' : 'Crear Usuario'}</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Nombre:
          <input type="text" name="nombre" value={usuario.nombre} onChange={handleInputChange} required />
        </label>
        <label>
          Email:
          <input type="email" name="email" value={usuario.email} onChange={handleInputChange} required />
        </label>
        <button type="submit">{idUsuario ? 'Actualizar' : 'Crear'}</button>
      </form>
    </div>
  );
};

export default UsuarioForm;
