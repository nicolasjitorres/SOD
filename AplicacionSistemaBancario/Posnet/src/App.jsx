import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import Home from './components/Home';
import SistemaBancario from './components/SistemaBancario';
import Cajero from './components/Cajero';
import Posnet from './components/Posnet';
import Deposito from './components/Deposito';
import Retiro from './components/Retiro';
import Transferencia from './components/Transferencia';
import HistorialTransferencias from './components/HistorialTransferencias';
import ConsultaSaldo from './components/ConsultaSaldo';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/sistema-bancario" element={<SistemaBancario />} />
        <Route path="/cajero" element={<Cajero />} />
        <Route path="/posnet" element={<Posnet />} />
        <Route path="/deposito" element={<Deposito />} />
        <Route path="/retiro" element={<Retiro />} />
        <Route path="/transferencia" element={<Transferencia />} />
        <Route path="/historial" element={<HistorialTransferencias />} />
        <Route path="/consulta" element={<ConsultaSaldo />} /> {/* Ruta para la nueva vista */}
      </Routes>
    </Router>
  );
}

export default App;
