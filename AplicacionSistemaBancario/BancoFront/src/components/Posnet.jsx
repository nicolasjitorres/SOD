import { useNavigate } from 'react-router-dom';

function Posnet() {
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    navigate("/");
  }

  const handleTransfer = async () => {
    navigate("/transferencia");
  };

  return (
    <div>
      <h2>Posnet</h2>
      <h3>Bienvenido de nuevo, usuario {accountId}</h3>
      <h4>¿Que accion desea realizar?</h4>
      <button onClick={handleTransfer}>Pagar</button>
    </div>
  );
}

export default Posnet;
