import React, { useState } from 'react';

const RegisterForm = () => {
  const [name, setName] = useState('');
  const [pin, setPin] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const res = await fetch('/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({ name, pin }),
    });

    const result = await res.text();
    alert(result);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form
        onSubmit={handleSubmit}
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
            min="1000"
            max="9999"
          />
        </label>

        <button
          type="submit"
          className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
        >
          Register
        </button>
      </form>
    </div>
  );
};

export default RegisterForm;
