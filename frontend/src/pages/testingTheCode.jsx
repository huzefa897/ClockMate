import React from 'react'
import { useState } from 'react';

const testingTheCode = () => {
const [pin, setPin] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/attendance/record?pin=${pin}`, {
        method: 'POST',
      });

      if (response.ok) {
        const result = await response.text();
        setMessage(result);
      } else {
        setMessage('Error recording attendance');
      }
    } catch (error) {
      console.error('Error:', error);
      setMessage('Server error');
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <input
          type="password"
          placeholder="Enter PIN"
          value={pin}
          onChange={(e) => setPin(e.target.value)}
          className="border p-2 rounded"
        />
        <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
          Submit
        </button>
      </form>
      {message && <p className="mt-4 text-lg">{message}</p>}
    </div>
  );
}


export default testingTheCode;