import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';
import { Form } from 'react-bootstrap';

const MultiSelect = ({
    label,
    name,
    options,
    selectedValues,
    onChange,
    className,
    disabled = false,
    required = false,
    ...rest
}) => {

    const [currentSelectedOptions, setCurrentSelectedOptions] = useState([]);

    useEffect(() => {
        const initialOptionsToSet = [];

        if (selectedValues && selectedValues.length > 0) {

            selectedValues.forEach(selectedOption => {

                const foundOption = options.find(opt => opt.id === selectedOption.value);
                
                console.log("MultiSelect (useEffect): Ищем ID:", selectedOption.value, "Найденная опция:", foundOption); // Для отладки
                
                if (foundOption) {
                    initialOptionsToSet.push({
                        value: foundOption.id,
                        label: foundOption.description
                    });
                } else {

                    initialOptionsToSet.push({
                        value: selectedOption.value,
                        label: selectedOption.label
                    });
                }
            });
        }
        
        setCurrentSelectedOptions(initialOptionsToSet);

        if (initialOptionsToSet.length != 0) {
            onChange(initialOptionsToSet.values);
        }

    }, [selectedValues, options]);

    

    const handleChange = (event) => {
        const optionsToSet = [];

        const { options } = event.target;

        for (let i = 0; i < options.length; i++) {
            if (options[i].selected) {

                optionsToSet.push({
                        value: options[i].value,
                        label: options[i].label
                    });
            }
        }
        setCurrentSelectedOptions(optionsToSet);

        if (onChange){
            const selectedValuesAsIds = optionsToSet.map(option => option.value); 
            onChange(selectedValuesAsIds);
        }
    };


    return (
        <Form.Group className={`mb-2 ${className || ''}`} controlId={name}>
            {label && <Form.Label className='fw-bold mb-2'>{label}</Form.Label>}
            <Form.Select
                multiple
                name={name || ''}
                value={currentSelectedOptions.map(opt => String(opt.value))}
                onChange={handleChange}
                disabled={disabled}
                required={required}
                {...rest}
            >
                {options.map(option => (
                    <option key={option.id} value={option.id}>
                        {option.description}
                    </option>
                ))}
            </Form.Select>
        </Form.Group>
    );
};

MultiSelect.propTypes = {
    label: PropTypes.string,
    name: PropTypes.string.isRequired,
    options: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
            description: PropTypes.string.isRequired, 
        })
    ).isRequired,
    selectedValues: PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.number, PropTypes.string])).isRequired,
    onChange: PropTypes.func.isRequired,
    className: PropTypes.string,
    disabled: PropTypes.bool,
    required: PropTypes.bool,
};

export default MultiSelect;