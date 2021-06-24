package one.digitalinnovation.personapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO){

        Person personToSave = PersonMapper.INSTANCE.toModel(personDTO);

        Person savedPerson = personRepository.save(personToSave);
        return createMessageResponse(savedPerson.getId(), "Created Person with ID ");
    }


    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {

        /*Optional<Person> optionalPerson = personRepository.findById(id);

        if(optionalPerson.isEmpty()){
            throw new PersonNotFoundException(id);
        }
        return personMapper.toDTO(optionalPerson.get());*/

        Person person = verifyIfExists(id);

        return  personMapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException {

        verifyIfExists(id);

        personRepository.deleteById(id);
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {

        verifyIfExists(id);

        Person personToUpdate = PersonMapper.INSTANCE.toModel(personDTO);

        Person updatedPerson = personRepository.save(personToUpdate);
        return createMessageResponse(updatedPerson.getId(), "Updated Person with ID ");
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
