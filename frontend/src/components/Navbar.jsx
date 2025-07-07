import { Link } from 'react-router-dom';
import logo from '../assets/img/Logo.png'; // use relative import instead of string path

function Navbar() {
  return (
    <nav className="bg-white shadow-md flex items-center justify-between py-3 px-10 fixed top-0 left-0 w-full z-50">
      {/* Logo and brand name */}

        <img src={logo} alt="Logo" className="h-20" />



      {/* Navigation links */}
      <div className="text-[15px] font-bold text-[#B2A6AB] flex items-center space-x-8 text-gray-700 text-sm">
        <Link to="/" className="hover:underline px-20 text-[#DAB2B3]">Home</Link>
        <Link to="/dashboard" className="hover:underline px-20">Dashboard</Link>

         <a href="https://pearldentalcare.com.au/about-us/" className="hover:underline px-20" >About Us</a>
      </div>
    </nav>
  );
}

export default Navbar;
