import React, { useState } from 'react';
import SearchBar from './components/SearchBar';
import WeatherDisplay from './components/WeatherDisplay';
import WeatherDetails from './components/WeatherDetails';
import { fetchWeather } from './services/weatherApi';

function App() {
  const [weatherData, setWeatherData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSearch = async (city) => {
    if (!city.trim()) {
      setError('Please enter a city name');
      return;
    }

    setLoading(true);
    setError(null);
    setWeatherData(null);

    try {
      const data = await fetchWeather(city);
      setWeatherData(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <div className="container">
        <header className="header">
          <h1>🌤️ Weather App</h1>
          <p>Get current weather and 5-day forecast for any city</p>
        </header>

        <SearchBar onSearch={handleSearch} loading={loading} />

        {error && (
          <div className="error-message">
            <p>❌ {error}</p>
          </div>
        )}

        {loading && (
          <div className="loading">
            <p>Loading...</p>
          </div>
        )}

        {weatherData && !loading && (
          <>
            <WeatherDisplay data={weatherData} />
            <WeatherDetails data={weatherData} />
          </>
        )}
      </div>
    </div>
  );
}

export default App;