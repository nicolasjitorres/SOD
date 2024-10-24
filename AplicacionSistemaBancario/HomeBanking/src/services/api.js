import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// Obtener todas las cuentas
export const obtenerCuentas = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/cuentas`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener las cuentas:', error);
    throw error;
  }
};

// Obtener todas las cuentas
export const obtenerCuentaPorId = async (idCuenta) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/cuentas/${idCuenta}`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener la cuenta:', error);
    throw error;
  }
};

// Crear una nueva cuenta
export const crearCuenta = async (cuenta) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/cuentas`, cuenta);
    return response.data;
  } catch (error) {
    console.error('Error al crear la cuenta:', error);
    throw error;
  }
};

// Actualizar una cuenta existente
export const actualizarCuenta = async (idCuenta, cuentaActualizada) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/cuentas/${idCuenta}`, cuentaActualizada);
    return response.data;
  } catch (error) {
    console.error('Error al actualizar la cuenta:', error);
    throw error;
  }
};

// Eliminar una cuenta
export const eliminarCuenta = async (idCuenta) => {
  try {
    await axios.delete(`${API_BASE_URL}/cuentas/${idCuenta}`);
  } catch (error) {
    console.error('Error al eliminar la cuenta:', error);
    throw error;
  }
};

export const obtenerMovimientosDeCuenta = async (idCuenta) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/cuentas/${idCuenta}/movimientos`);
    return response.data;
  } catch (error) {
    console.error(`Error al obtener movimientos para la cuenta ${idCuenta}:`, error);
    throw error;
  }
};

export const obtenerUsuarios = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/usuarios`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener los usuarios:', error);
    throw error;
  }
};

export const obtenerUsuarioPorId = async (idUsuario) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/usuarios/${idUsuario}`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener el usuario:', error);
    throw error;
  }
};

export const crearUsuario = async (usuario) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/usuarios`, usuario);
    return response.data;
  } catch (error) {
    console.error('Error al crear el usuario:', error);
    throw error;
  }
};

export const actualizarUsuario = async (idUsuario, usuarioActualizado) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/usuarios/${idUsuario}`, usuarioActualizado);
    return response.data;
  } catch (error) {
    console.error('Error al actualizar el usuario:', error);
    throw error;
  }
};

export const eliminarUsuario = async (idUsuario) => {
  try {
    await axios.delete(`${API_BASE_URL}/usuarios/${idUsuario}`);
  } catch (error) {
    console.error('Error al eliminar el usuario:', error);
    throw error;
  }
};