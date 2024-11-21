import axios from "axios";

const servers = [
  // "http://localhost:8080",
  // "http://localhost:8081",
  // "http://localhost:8082",
  "http://sd.sod:8080",
  "http://sd.sod:8081",
  "http://sd.sod:8082",
];

let mensaje = "No se pudo conectar a ningún servidor.";

const hacerSolicitud = async (method, url, data = null) => {
  for (let server of servers) {
    try {
      const config = {
        method,
        url: `${server}${url}`,
        data,
      };
      const response = await axios(config);
      return response.data;
    } catch (error) {
      if (error.response) {
        console.warn(`Error en el servidor ${server}: ${error.response.data}`);
        mensaje = error.response.data;
        break;
      } else {
        console.warn(`Error en el servidor ${server}: ${error.message}`);
      }
    }
  }
  throw new Error(mensaje);
};

// Obtener todas las cuentas
export const obtenerCuentas = async () => {
  return await hacerSolicitud("get", "/cuentas");
};

// Obtener una cuenta por ID
export const obtenerCuentaPorId = async (idCuenta) => {
  return await hacerSolicitud("get", `/cuentas/${idCuenta}`);
};

// Crear una nueva cuenta
export const crearCuenta = async (cuenta) => {
  return await hacerSolicitud("post", "/cuentas", cuenta);
};

// Actualizar una cuenta existente
export const actualizarCuenta = async (idCuenta, cuentaActualizada) => {
  return await hacerSolicitud("put", `/cuentas/${idCuenta}`, cuentaActualizada);
};

// Eliminar una cuenta
export const eliminarCuenta = async (idCuenta) => {
  return await hacerSolicitud("delete", `/cuentas/${idCuenta}`);
};

// Obtener movimientos de una cuenta
export const obtenerMovimientosDeCuenta = async (idCuenta) => {
  return await hacerSolicitud("get", `/cuentas/${idCuenta}/movimientos`);
};

// Obtener todos los usuarios
export const obtenerUsuarios = async () => {
  return await hacerSolicitud("get", "/usuarios");
};

// Obtener un usuario por ID
export const obtenerUsuarioPorId = async (idUsuario) => {
  return await hacerSolicitud("get", `/usuarios/${idUsuario}`);
};

// Crear un nuevo usuario
export const crearUsuario = async (usuario) => {
  return await hacerSolicitud("post", "/usuarios", usuario);
};

// Actualizar un usuario existente
export const actualizarUsuario = async (idUsuario, usuarioActualizado) => {
  return await hacerSolicitud(
    "put",
    `/usuarios/${idUsuario}`,
    usuarioActualizado
  );
};

// Eliminar un usuario
export const eliminarUsuario = async (idUsuario) => {
  return await hacerSolicitud("delete", `/usuarios/${idUsuario}`);
};

// Método de inicio de sesión
export const iniciarSesion = async (accountId, password) => {
  return await hacerSolicitud("post", "/login", { accountId, password });
};

export const cerrarSesion = async (accountId) => {
  return await hacerSolicitud("post", "/logout", { accountId });
};
