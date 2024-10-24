import { useNavigate } from 'react-router-dom';

function SistemaBancario() {
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
      <h2>Homebanking</h2>
      <h3>Bienvenido de nuevo, usuario {accountId}</h3>
      <h4>¿Que accion desea realizar?</h4>
      <button onClick={handleTransfer}>Transferir</button>
    </div>
  );
}

export default SistemaBancario;
