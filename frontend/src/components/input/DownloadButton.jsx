import PropTypes from 'prop-types';
import { useState } from 'react';
import { Button, Spinner } from 'react-bootstrap';

const DownloadButton = ({
    url,
    fileName,
    buttonText,
    variant = 'primary',
    className = '',
    icon: IconComponent,
    ...rest
}) => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleDownload = async () => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await fetch(url);

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Ошибка сети или сервера: ${response.status} ${response.statusText} - ${errorText}`);
            }

            const contentDisposition = response.headers.get('Content-Disposition');
            let actualFileName = fileName;
            if (contentDisposition) {
                const fileNameMatch = contentDisposition.match(/filename\*?=['"]?([^"';\n]*)/i);
                if (fileNameMatch && fileNameMatch[1]) {
                    actualFileName = decodeURIComponent(fileNameMatch[1].replace(/^utf-8''/, ''));
                }
            }

            const blob = await response.blob();

            const downloadUrl = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.setAttribute('download', actualFileName);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(downloadUrl);
        } catch (err) {
            console.error("Ошибка при скачивании файла:", err);
            setError(err.message || "Не удалось загрузить файл.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <Button
            variant={variant}
            onClick={handleDownload}
            disabled={isLoading}
            className={className}
            {...rest}
        >
            {isLoading ? (
                <>
                    <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        role="status"
                        aria-hidden="true"
                        className="me-2"
                    />
                    Загрузка...
                </>
            ) : (
                <>
                    {IconComponent && <IconComponent className="me-2" />}
                    {buttonText}
                </>
            )}
            {error && <div className="text-danger mt-2">{error}</div>}
        </Button>
    );
};

DownloadButton.propTypes = {
    url: PropTypes.string.isRequired,
    fileName: PropTypes.string.isRequired,
    buttonText: PropTypes.string.isRequired,
    variant: PropTypes.string,
    className: PropTypes.string,
    icon: PropTypes.elementType,
};

export default DownloadButton;