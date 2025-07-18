"use client"
import React, { useState } from 'react';

const SearchBar: React.FC = () => {
  const [query, setQuery] = useState('');

  const handleSearch = () => {
    alert(`Searching for: ${query}`);
  };

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'column',
        padding: '20px',
        minHeight: '100vh',
        boxSizing: 'border-box',
      }}
    >
      <div
        style={{
          display: 'flex',
          width: '100%',
          maxWidth: '600px', // caps the width on large screens
          padding: '0 10px',
        }}
      >
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search..."
          style={{
            flex: 1,
            padding: '12px 16px',
            borderRadius: '25px 0 0 25px',
            border: '1px solid #ccc',
            fontSize: '16px',
          }}
        />
        <button
          onClick={handleSearch}
          style={{
            padding: '12px 20px',
            borderRadius: '0 25px 25px 0',
            border: '1px solid #ccc',
            backgroundColor: '#007bff',
            color: '#fff',
            cursor: 'pointer',
            fontSize: '16px',
            borderLeft: 'none',
          }}
        >
          Search
        </button>
      </div>
    </div>
  );
};

export default SearchBar;
