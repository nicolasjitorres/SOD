import { Link, useNavigate } from "react-router-dom";
import { cerrarSesion } from "../../../Posnet/src/services/api";

function Home() {
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    navigate("/");
  }
  const handleClick = async () => {
    try {
      await cerrarSesion(accountId);
      localStorage.clear();
      navigate("/");
    } catch (error) {
      console.error("Error al cerrar sesión:", error);
    }
  };

  return (
    <div>
      <h2>Bienvenido al Posnet</h2>
      <h3>Numero de cuenta: {accountId}</h3>
      <Link to="/posnet">
        <button>Acciones</button>
      </Link>
      <br />
      <Link to="/historial">
        <button>Historial de Transferencias</button>
      </Link>
      <Link to={"/consulta"}>
        <button>Consultar Saldo</button>
      </Link>
      <br />
      <br />
      <div>
        <button onClick={handleClick}>Cerrar sesión</button>
      </div>
    </div>
  );
}

export default Home;
