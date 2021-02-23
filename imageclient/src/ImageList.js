import React from 'react';
import axios from 'axios';
import Container from 'react-bootstrap/Container';
import Alert from 'react-bootstrap/Alert';
import Image from 'react-bootstrap/Image';
import Table from 'react-bootstrap/Table';
import Modal from 'react-bootstrap/Modal';

//Get API URL from Process
export const API_URL = process.env.REACT_APP_API_URL || "https://localhost:8080/imageapi/";

export default class ImageList extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            imagelist: [],
            iserror: false,
            error: '',
            imageselected: null,
            show: false
        };
        this.getImages = this.getImages.bind(this);
        this.handleImageClick = this.handleImageClick.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    componentDidMount() {
        this.getImages().then((data) => {
            this.setState({ imagelist: data });

        }).catch((error) => {
            console.log('Error processing image list request: ' + error);
            this.setState({ iserror: true, error: "Error invoking image service api." })
        });
    }

    handleImageClick = (imageid) => {
        this.getImage(imageid).then((data) => {
            this.setState({ imageselected: URL.createObjectURL(data), show: true });
        }).catch((error) => {
            console.log('Error processing image list request: ' + error);
            this.setState({ iserror: true, error: "Error invoking image service api." })
        });
    };

    handleClose = () => {
        this.setState({ imageselected: null, show: false });
    }

    getImages = async () => {
        let data = [];
        const response = await axios.get(API_URL + "images")
        if (response.data) {
            response.data.forEach(imagedata => {
                data.push({
                    imageid: imagedata.imageid,
                    imagedate: imagedata.dateadded,
                    image: "data:image/png;base64," + imagedata.image
                });
            });
        }
        return data;
    }

    getImage = async (imageID) => {
        const response = await axios.get(API_URL + "images/" + imageID, {
            responseType: 'blob',
            headers: {
                "accept": "application/octet-stream"
            }
        });
        if (response.data) {
            return response.data;
        }
        return null;
    }

    render() {
        return (
            <Container>
                <Container>&nbsp;</Container>
                <Container>
                    <Alert show={this.state.iserror} variant="danger"><p>{this.state.error}</p></Alert>
                </Container>
                <Table striped responsive bordered variant="dark" size="sm">
                    <thead>
                        <tr>
                            <th className="text-center">Image ID</th>
                            <th className="text-center">Date Added</th>
                            <th className="text-center">Image</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.imagelist.map((imagedata, index) => {
                            return (
                                <tr key={imagedata.imageid}>
                                    <td>{imagedata.imageid}</td>
                                    <td>{imagedata.imagedate}</td>
                                    <td><Image alt="uploaded" src={imagedata.image} onClick={() => this.handleImageClick(imagedata.imageid)} /></td>
                                </tr>
                            );
                        })}
                    </tbody>
                </Table>
                <Modal size="xl" dialogClassName="modal-90w" scrollable="true" show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton></Modal.Header>
                    <Modal.Body><p>{this.state.imageselected ? (<Image alt="uploaded" src={this.state.imageselected} />) : ""}</p></Modal.Body>
                </Modal>
            </Container>
        );
    }
}