import React, { Component } from "react";
import {
  Container,
  Header,
  Title,
  Content,
  Button,
  Footer,
  FooterTab,
  List,
  ListItem,
  Text,
  Body,
  Left,
  Right,
  Icon
} from "native-base";
import styles from "./styles";
import {httpGet} from "../../FetchUtil";
import {baseAddress, STATUS} from "../../common";

const ddlBase = baseAddress + "/ddl";
const updateBase = baseAddress +"/infoupdate"
var DDLArray=[
  {
    "classN":"1",
    "homework":"2",
    "information":"3"
  },
  {
    "classN":"2",
    "homework":"3",
    "information":"4"
  }
];
var UPDATEINFOArray=[
  {
    "classN":"3",
    "homework":"2",
    "information":"1"
  },
  {
    "classN":"4",
    "homework":"3",
    "information":"2"
  }
];
var listShow;
export default class IconText extends Component {

  constructor(props) {
    super(props);
    this.state = {
      tab1: true,
      tab2: false,
    };
    httpGet(ddlBase,(result1) => {
        DDLArray = result1.data;
        //console.warn(DDLArray);
      }
    )
    httpGet(updateBase,(result2) =>{
      UPDATEINFOArray = result2.data;
      //console.warn(UPDATEINFOArray);
    })
  }
  toggleTab1() {
    listShow = DDLArray;
    this.setState({
      tab1: true,
      tab2: false,
    });
    //console.warn(listShow);
  }
  toggleTab2() {
    listShow = UPDATEINFOArray;
    this.setState({
      tab1: false,
      tab2: true,
    });
    //console.warn(listShow);
  }
  render() {




    return (
      <Container style={styles.container}>
        <Header>
          <Left>
            <Title>我的信息</Title>
          </Left>
          <Body>
          </Body>
          <Right />
        </Header>

        <Content padder style={{backgroundColor: "white"}}>
          { this.state.tab1?
            <List
              dataArray={DDLArray}
              renderRow={data =>
                <ListItem avatar>
                  <Body>
                  <Text>
                    {data.homework}
                  </Text>
                  <Text numberOfLines={1} note>
                    {data.information}
                  </Text>
                  </Body>
                  <Right>
                    <Text note>
                      {data.classN}
                    </Text>
                  </Right>
                </ListItem>}
            />:<List
              dataArray={UPDATEINFOArray}
              renderRow={data =>
                <ListItem avatar>
                  <Body>
                  <Text>
                    {data.homework}
                  </Text>
                  <Text numberOfLines={1} note>
                    {data.information}
                  </Text>
                  </Body>
                  <Right>
                    <Text note>
                      {data.classN}
                    </Text>
                  </Right>
                </ListItem>}
            />
          }
        </Content>

        <Footer>
          <FooterTab>
            <Button active={this.state.tab1} onPress={() => this.toggleTab1()}>
              <Icon active={this.state.tab1} name="apps" />
              <Text>DDL</Text>
            </Button>
            <Button active={this.state.tab2} onPress={() => this.toggleTab2()}>
              <Icon active={this.state.tab2} name="camera" />
              <Text>课程信息</Text>
            </Button>
          </FooterTab>
        </Footer>
      </Container>
    );
  }
}

