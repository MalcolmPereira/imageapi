import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Container from 'react-bootstrap/Container';
import './App.css';
import ImageUpload from "./ImageUpload";
import ImageList from "./ImageList";

export default class App extends React.Component {

  constructor(props, context) {
    super(props, context);
    this.state = {
      key: 'imageList',
    };
    this.handleSelect = this.handleSelect.bind(this);
  }

  handleSelect = (selectedKey) => {
    this.setState({ key: selectedKey });
  }

  render() {
    return (
      <div className="App">
        <Container>
          <Navbar fixed="top" expand="lg" bg="dark" variant="dark">
            <Navbar.Brand href="#home">Image Sampling Client</Navbar.Brand>
          </Navbar>
          <br></br>
          <br></br>
          <br></br>
          <Nav variant="pills" activeKey={this.state.key} onSelect={this.handleSelect} >
            <Nav.Item >
              <Nav.Link eventKey="imageList">Image List</Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link eventKey="imageSampling">Image Sampling</Nav.Link>
            </Nav.Item>
          </Nav>
          {this.state.key === "imageSampling" ? <ImageUpload /> : <ImageList />}
        </Container>
      </div >
    );
  }
}
