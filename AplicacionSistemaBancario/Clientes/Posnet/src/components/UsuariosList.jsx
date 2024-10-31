import { useEffect, useState } from 'react';
import { obtenerUsuarios, eliminarUsuario } from '../services/api';
import { Link } from 'react-router-dom';

const UsuariosList = () => {
  const [usuarios, setUsuarios] = useState([]);

  useEffect(() => {
    const fetchUsuarios = async () => {
      try {
        const data = await obtenerUsuarios();
        setUsuarios(data);
      } catch (error) {
        console.error('Error al cargar los usuarios', error);
      }
    };
    fetchUsuarios();
  }, []);

  const handleEliminarUsuario = async (idUsuario) => {
    try {
      await eliminarUsuario(idUsuario);
      setUsuarios(usuarios.filter(usuario => usuario.id !== idUsuario));
    } catch (error) {
      console.error('Error al eliminar el usuario', error);
    }
  };

  return (
    <div>
      <h2>Lista de Usuarios</h2>
      <Link to="/usuarios/crear">Crear nuevo usuario</Link>
      <ul>
        {usuarios.map(usuario => (
          <li key={usuario.id}>
            {usuario.nombre} - {usuario.email}
            <button onClick={() => handleEliminarUsuario(usuario.id)}>Eliminar</button>
            <Link to={`/usuarios/${usuario.id}/editar`}>Editar</Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UsuariosList;
