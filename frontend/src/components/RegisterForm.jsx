import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

const RegisterForm = () => {
  const [name, setName] = useState('');
  const [pin, setPin] = useState('');
  const [error, setError] = useState('');
  const [status, setStaus] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault(); // <-- this prevents the default form submit

    if (!pin) {
      setError("Pin is required");
      return;
    }

    try {
      const formData = new URLSearchParams();
      formData.append('name', name);
      formData.append('pin', pin);

      const response = await axios.post('http://localhost:8080/Register', formData, {
        headers: {
          "Content-Type": 'application/x-www-form-urlencoded',
        },
      });

      alert(response.data);
      console.log(response.data);
      setError('');
      setStaus(`✅ ${status}`);
      navigate('/');
    } catch (error) {
      console.error(error);
      setError('Registration Failed');
    }
  };


  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form
        className="bg-white p-6 rounded-lg shadow-md w-full max-w-sm"
      >
        <h2 className="text-2xl font-bold mb-4 text-center">Register</h2>

        <label className="block mb-2 text-sm font-medium text-gray-700">
          Name
          <input
            type="text"
            className="w-full mt-1 p-2 border border-gray-300 rounded"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </label>

        <label className="block mb-4 text-sm font-medium text-gray-700">
          4-Digit PIN
          <input
            type="number"
            className="w-full mt-1 p-2 border border-gray-300 rounded"
            value={pin}
            onChange={(e) => setPin(e.target.value)}
            required

          />
        </label>
        {error && <p className="text-red-500 mt-2">{error}</p>}
        {status && <p className="text-green-600 mt-2">{status}</p>}
        <button
          type="submit"
          onClick={handleSubmit}
          className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
        >
          Register
        </button>
      </form>
    </div>
  );
};

export default RegisterForm;
