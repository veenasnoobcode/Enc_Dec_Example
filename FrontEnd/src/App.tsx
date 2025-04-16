import React, { useState } from "react";
import "./App.css";

interface EncryptionResponse {
  key: string;
  value: string;
}

const App: React.FC = () => {
  const [inputText, setInputText] = useState<string>("");
  const [encryptedText, setEncryptedText] = useState<string>("");
  const [encryptionKey, setEncryptionKey] = useState<string>("");
  const [decryptedText, setDecryptedText] = useState<string>("");
  const [decryptionKey, setDecryptionKey] = useState<string>("");
  const [error, setError] = useState<string>("");

  const handleEncrypt = async (): Promise<void> => {
    try {
      const response = await fetch("http://localhost:8080/api/encrypt", {
        method: "POST",
        headers: {
          "Content-Type": "text/plain",
          Accept: "application/json",
        },
        body: inputText,
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Encryption failed");
      }

      const data: EncryptionResponse = await response.json();
      setEncryptedText(data.value);
      setEncryptionKey(data.key);
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    }
  };

  const handleDecrypt = async (): Promise<void> => {
    try {
      const response = await fetch("http://localhost:8080/api/decrypt", {
        method: "POST",
        headers: {
          "Content-Type": "text/plain",
          Accept: "application/json",
        },
        body: encryptedText,
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Decryption failed");
      }

      const data: EncryptionResponse = await response.json();
      setDecryptedText(data.value);
      setDecryptionKey(data.key);
      setError("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Welcome to HoneyWell</h1>
        <div className="container">
          <div className="input-section">
            <h3>Input Text:</h3>
            <textarea
              value={inputText}
              onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) =>
                setInputText(e.target.value)
              }
              placeholder="Enter text to encrypt"
              rows={4}
            />
            <button onClick={handleEncrypt}>Encrypt</button>
          </div>

          <div className="result-section">
            <h3>Encrypted Result:</h3>
            <div className="key-value-pair">
              <div className="label">Key:</div>
              <input type="text" value={encryptionKey} readOnly />
            </div>
            <div className="key-value-pair">
              <div className="label">Encrypted Text:</div>
              <textarea value={encryptedText} readOnly rows={4} />
            </div>
            <button onClick={handleDecrypt}>Decrypt</button>
          </div>

          <div className="result-section">
            <h3>Decrypted Result:</h3>
            <div className="key-value-pair">
              <div className="label">Key:</div>
              <input type="text" value={decryptionKey} readOnly />
            </div>
            <div className="key-value-pair">
              <div className="label">Decrypted Text:</div>
              <textarea value={decryptedText} readOnly rows={4} />
            </div>
          </div>

          {error && <div className="error">{error}</div>}
        </div>
      </header>
    </div>
  );
};

export default App;
