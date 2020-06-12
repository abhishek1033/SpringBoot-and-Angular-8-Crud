package com.spring.crud.rest.api.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.crud.rest.api.exception.ResourceNotFoundException;
import com.spring.crud.rest.api.model.Employee;
import com.spring.crud.rest.api.model.ImageModel;
import com.spring.crud.rest.api.repository.EmployeeRepository;
import com.spring.crud.rest.api.repository.ImageRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController 
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Api(value="Employee", description="Employee create,Upldate,Delete,Read...")
public class MainController
{
	@Autowired
    private EmployeeRepository employeeRepository;
	
	
	@Autowired
	ImageRepository imageRepository;
	
	
	@ApiOperation(value = "View a list of available products",response = Iterable.class)
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	
	
	@GetMapping("/employees")
	@Cacheable(value="customerInfo") 
    public List<Employee> getAllEmployees() 
	{
        return employeeRepository.findAll();
    }
	
	@ApiOperation(value = "Search a employee with an ID",notes="search employee details by entering employee id...." ,response = Employee.class)
	@GetMapping("/employees/{id}")
	@Cacheable(value="customerInfo")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException 
	{
        Employee employee = employeeRepository.findById(employeeId)
          .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }
	
	@ApiOperation(value = "Add a new Employee",notes="enter required details of employee to register new employee..")
	@PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee) 
	{
        return employeeRepository.save(employee);
    }
	
	
	@ApiOperation(value = "update already registerd Employee ",notes="enter required details of already registerd employee to update teh employee data..")
	@PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId, @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException 
	{
        Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }
	
	@ApiOperation(value = "delete employee by ID ",notes="Delete employee by entering employee ID if exist..")
	@DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException 
	{
        Employee employee = employeeRepository.findById(employeeId)
       .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
	
	
	
	@ApiOperation(value = "Upload Image file ",notes="Upload new image file in database..")
	@PostMapping("/upload")
	public ResponseEntity<Void> uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException 
	{
		System.out.println("Original Image Byte Size - " + file.getBytes().length);
			
		ImageModel img1=new ImageModel();
		img1.setName(file.getOriginalFilename());
		img1.setType(file.getContentType());
		img1.setPicByte(compressBytes(file.getBytes()));
		img1.setCrt_date(new Date());
		imageRepository.save(img1);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@ApiOperation(value = "Search Image File ",notes="Search Image File by entering image name..")
	@GetMapping(path = { "/get/{imageName}" })
	@Cacheable(value="customerInfo")
	public ImageModel getImage(@PathVariable("imageName") String imageName) throws IOException 
	{
		final Optional<ImageModel> retrievedImage = imageRepository.findByName(imageName);
		ImageModel img = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
				decompressBytes(retrievedImage.get().getPicByte()));
		return img;
	}

	private byte[] decompressBytes(byte[] picByte) 
	{
		Inflater inflater = new Inflater();
		inflater.setInput(picByte);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(picByte.length);
		byte[] buffer = new byte[1024];
		try 
		{
			while (!inflater.finished()) 
			{
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} 
		catch (IOException ioe) 
		{
			ioe.printStackTrace();
		} 
		catch (DataFormatException e) 
		{
			e.printStackTrace();
		}
		return outputStream.toByteArray();
		
	}

	private static byte[] compressBytes(byte[] bytes) 
	{
		Deflater deflater = new Deflater();
		deflater.setInput(bytes);
		deflater.finish();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) 
		{
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try 
		{
			outputStream.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
		return outputStream.toByteArray();
	}
	
	
	
	
}
