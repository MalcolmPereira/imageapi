import React from 'react';
import axios from 'axios';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import Spinner from 'react-bootstrap/Spinner';
import Alert from 'react-bootstrap/Alert';
import Table from 'react-bootstrap/Table';
import Image from 'react-bootstrap/Image';
import Form from 'react-bootstrap/Form';

//Get API URL from Process
export const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/imageapi/";

export default class ImageUpload extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            selectedFile: null,
            loading: false,
            image: null,
            imagescaled: null,
            imageHeight: null,
            imageWidth: null,
            imageSize: null,
            imageMime: null,
            scale: 1.00,
            scaleType: "BICUBIC",
            scaleout: false,
            iserror: false,
            error: '',
        };
        this.handleFileChange = this.handleFileChange.bind(this);
        this.handleScaleChange = this.handleScaleChange.bind(this);
        this.handleScaleTypeChange = this.handleScaleTypeChange.bind(this);
        this.downloadFile = this.downloadFile.bind(this);
    }

    handleFileChange = event => {
        let imagefile = event.target.files[0];
        this.getImageDetails(imagefile).then((data) => {
            this.setState({
                image: imagefile,
                imageHeight: data.height,
                imageWidth: data.width,
                imageSize: data.size,
                imageMime: data.mimetype,
                selectedFile: URL.createObjectURL(imagefile),
                imagescaled: null,
                imagescaledSize: null,
                iserror: false,
                error: '',
            });
        }).catch((error) => {
            this.setState({ iserror: true, error: "Error invoking image validation service." })
            console.log('Error processing image upload request: ' + error);
        });
    };

    handleScaleTypeChange = event => {
        this.setState({
            scaleType: event.target.value,
            imagescaled: null,
            imagescaledSize: null,
            loading: true,
            iserror: false,
            error: '',
        });
        this.uploadimage(this.state.scale, event.target.value).then((data) => {
            this.setState({
                imagescaledSize: data.size,
                imagescaled: URL.createObjectURL(data),
                loading: false
            });
        }).catch((error) => {
            this.setState({
                iserror: true, error: "Error invoking image scaling service.",
                loading: false
            })
            console.log('Error processing image upload request: ' + error);
        });
    };

    handleScaleChange = event => {
        if (!this.state.loading) {
            this.setState({
                scale: event.target.value,
                imagescaled: null,
                imagescaledSize: null,
                loading: true,
                iserror: false,
                error: '',
            });
            this.uploadimage(event.target.value).then((data) => {
                this.setState({
                    imagescaledSize: data.size,
                    imagescaled: URL.createObjectURL(data),
                    loading: false
                });
            }).catch((error) => {
                this.readBlob(error.response.data).then((data) => {
                    this.setState({
                        iserror: true,
                        error: "Error invoking image scaling service: " + JSON.parse(data).message,
                        loading: false
                    });
                });
            });
        }
    };

    downloadFile = () => {
        let a = document.createElement('a');
        a.href = this.state.imagescaled;
        a.download = 'image.png';
        a.click();
    };

    readBlob = async (blob) => {
        let errorData = await (new Response(blob)).text();
        return errorData;
    };

    getImageDetails = async (imageData) => {
        const formData = new FormData();
        formData.append("image", imageData);
        const response = await axios.post(API_URL + "validate", formData);
        return response.data;
    }

    uploadimage = async (scale, scaletype) => {
        const formData = new FormData();
        formData.append("image", this.state.image);
        formData.append("scale", (scale) ? scale : this.state.scale);
        formData.append("scaletype", (scaletype) ? scaletype : this.state.scaleType);
        console.log("Image Scale: ", formData.get("scale"));
        console.log("Image scaleType: ", formData.get("scaletype"));
        const response = await axios.post(API_URL + "generate", formData, {
            responseType: 'blob',
            headers: {
                "content-type": "multipart/form-data",
                "accept": "application/json,application/octet-stream"
            }
        });
        return response.data;
    }

    render() {
        return (
            <Container>
                &nbsp;
                <Alert show={this.state.iserror} variant="danger"><p>{this.state.error}</p></Alert>
                <Table bordered striped variant="dark" size="sm">
                    <thead>
                        <tr>
                            <th class="text-center" style={{ width: "50%" }}>Original Image</th>
                            <th class="text-center" style={{ width: "50%" }}>Sampled Image</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td style={{ width: "50%" }}>
                                <Table responsive striped variant="dark" size="sm">
                                    <tbody>
                                        <tr>
                                            <td colSpan="4">
                                                <Form.Control type="file" size="sm" onChange={this.handleFileChange} />
                                            </td>
                                        </tr>
                                        {this.state.selectedFile &&
                                            <tr>
                                                <td>Width: {this.state.imageWidth} px</td>
                                                <td>Height: {this.state.imageHeight} px</td>
                                                <td>Size: {this.state.imageSize} b</td>
                                                <td>Type: {this.state.imageMime}</td>
                                            </tr>
                                        }
                                        {this.state.selectedFile &&
                                            <tr>
                                                <td colSpan="4">
                                                    <Form.Control type="range" id="imageScaling" name="imageScaling" value={this.state.scale} onChange={this.handleScaleChange} step="0.001" min="0.01" max="10" />
                                                </td>
                                            </tr>
                                        }
                                        {this.state.selectedFile &&
                                            <tr>
                                                <td style={{ width: "50%" }} colSpan="2">
                                                    <Form.Label>Scaling:</Form.Label>
                                                    <Form.Control as="select" size="sm" id="imageScalingType" name="imageScalingType" value={this.state.scaleType} onChange={this.handleScaleTypeChange}>
                                                        <option>BICUBIC</option>
                                                        <option>BILINEAR</option>
                                                        <option>NEAREST_NEIGHBOR</option>
                                                    </Form.Control>
                                                </td>
                                                <td style={{ width: "50%" }} colSpan="2">
                                                    Scaling:value={this.state.scale}
                                                    <br></br>
                                                    Width:  {Math.round(this.state.imageWidth * this.state.scale)}  px
                                                    <br></br>
                                                    Height: {Math.round(this.state.imageHeight * this.state.scale)} px
                                                    <br></br>
                                                    {this.state.imagescaledSize &&
                                                        <div>
                                                            Size: {this.state.imagescaledSize} b
                                                        </div>
                                                    }
                                                </td>
                                            </tr>
                                        }
                                        <tr>
                                            <td colSpan="4">
                                                <div style={{ border: "1px dotted black", overflow: "scroll", overflowX: "scroll", overflowY: "scroll", width: "500px", height: "550px" }}>
                                                    {this.state.selectedFile ? (<Image alt="uploaded" src={this.state.selectedFile} />) : "No File Uploaded"}
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </Table>
                            </td>
                            <td style={{ width: "50%" }}>
                                <Table borderless variant="dark" size="sm">
                                    <tbody>
                                        {this.state.imagescaled &&
                                            <tr>
                                                <td>
                                                    <Button variant="outline-primary" onClick={this.downloadFile}>Download</Button>
                                                </td>
                                            </tr>
                                        }
                                        <tr>
                                            <td>
                                                {this.state.loading ?
                                                    <Spinner animation="grow" />
                                                    :
                                                    <div style={{ border: "1px dotted black", overflow: "scroll", overflowX: "scroll", overflowY: "scroll", width: "550px", height: "600px" }}>
                                                        {this.state.imagescaled ? (<Image alt="uploaded" src={this.state.imagescaled} />) : "Not Available "}
                                                    </div>
                                                }
                                            </td>
                                        </tr>
                                    </tbody>
                                </Table>
                            </td>
                        </tr>
                    </tbody>
                </Table >
            </Container >
        );
    }
}