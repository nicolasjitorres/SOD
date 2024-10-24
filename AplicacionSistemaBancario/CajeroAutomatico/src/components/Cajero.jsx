import { useNavigate } from "react-router-dom";

function Cajero() {
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    navigate("/");
  }
  const handleDeposit = async () => {
    navigate("/deposito");
  };

  const handleWithdrawal = async () => {
    navigate("/retiro");
  };

  const handleTransfer = async () => {
    navigate("/transferencia");
  };

  return (
    <div>
      <h2>Cajero automático</h2>
      <h3>Bienvenido de nuevo, usuario {accountId}</h3>
      <h4>¿Que accion desea realizar?</h4>
      <button onClick={handleDeposit}>Depositar</button>
      <button onClick={handleWithdrawal}>Retirar</button>
      <button onClick={handleTransfer}>Transferir</button>
    </div>
  );
}

export default Cajero;
