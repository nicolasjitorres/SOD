import { Link, useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    navigate("/");
  }
  const handleClick = () => {
    localStorage.clear();
    navigate("/");
  }

  return (
    <div>
      <h2>Bienvenido al Sistema</h2>
      <h3>Numero de cuenta: {accountId}</h3>
      <Link to="/sistema-bancario">
        <button>Sistema Bancario</button>
      </Link>
      <Link to="/cajero">
        <button>Cajero</button>
      </Link>
      <Link to="/posnet">
        <button>Posnet</button>
      </Link>
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
