import React from 'react';

function WeatherDisplay({ data }) {
  const { name, main, weather } = data;
  const temperature = Math.round(main.temp);
  const feelsLike = Math.round(main.feels_like);
  const description = weather[0].description;
  const icon = weather[0].icon;

  return (
    <div className="weather-card">
      <div className="weather-main">
        <div className="weather-icon">
          <img
            src={`https://openweathermap.org/img/wn/${icon}@4x.png`}
            alt={description}
          />
        </div>
        <div className="weather-info">
          <h2 className="city-name">{name}</h2>
          <p className="weather-description">{description}</p>
          <div className="temperature">
            <span className="temp-value">{temperature}°C</span>
            <span className="feels-like">Feels like {feelsLike}°C</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default WeatherDisplay;