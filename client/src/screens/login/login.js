import React, {Component} from 'react';
import {
  StyleSheet,
  Dimensions,
  TouchableOpacity,
  View,
  Text,
  TextInput,
  Image,
  ToastAndroid,
  Navigator
} from 'react-native';

 import {baseAddress,STATUS} from "../../common";
 import { httpGet, httpPost } from "../../FetchUtil";


const ddlBase = baseAddress + "/ddl";
const updateBase = baseAddress +"/infoupdate";
const loginBase = baseAddress + '/loginCS';

let {width, height} = Dimensions.get('window');

export default class LoginView extends React.Component{
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: ''
    }
  }

  renderClick(){

    let userName = this.state.username;
    let password = this.state.password;
    let toastMsg = '登录成功';

    if(userName===null||userName===""){
      toastMsg = '用户名不能为空';
      ToastAndroid.showWithGravity(toastMsg, 1000, ToastAndroid.CENTER);
      return;
    }
    if(password===null||password===""){
      toastMsg = '密码不能为空';
      ToastAndroid.showWithGravity(toastMsg, 1000, ToastAndroid.CENTER);
      return;
    }

    //TODO: compare userName and password with 后台
    const params = {
      username: this.state.username,
      password: this.state.password,
    };


    let loginUrl = loginBase;

    httpPost(loginUrl, params, (result) => {
      const {status, data} = result;
      if (status === STATUS.SUCCESS) {
        toastMsg = '登录成功';
        ToastAndroid.showWithGravity(toastMsg, 1000, ToastAndroid.CENTER);
        //this.props.navigation.replace('SideMenu');
        this.props.navigation.replace('MyFooter');
      }
      else{
        toastMsg = '用户名或密码错误';
        ToastAndroid.showWithGravity(toastMsg, 1000, ToastAndroid.CENTER);
      }
      console.warn(result);

      //callback && callback(status);//TODO: complete callback
    })
  }

  render(){

    return(
      <View style={styles.container}>

        <TextInput placeholder='请输入用户名' clearButtonMode="always" underlineColorAndroid="transparent"
                   style={styles.inputStyle}
                   onChangeText={(username) => this.setState({username})}
        />

        <TextInput placeholder='请输入密码' clearButtonMode="always" secureTextEntry={true} password={true}
                   underlineColorAndroid="transparent" style={styles.inputStyle}
                   onChangeText={(password) => this.setState({password})}
        />

        <TouchableOpacity
          activeOpacity={0.5}
          onPress={() => this.renderClick()}
        >
          <View style={styles.loginBtnStyle}>
            <Text style={{color: 'white', fontSize: 20, fontWeight: '500'}}>登录</Text>
          </View>
        </TouchableOpacity>

      </View>
    );/*TODO: may need to add functions here, both 忘记密码 and 用户注册*/
  }

}


const styles = StyleSheet.create({

  container: {
    width: width,
    flex: 1,
    backgroundColor: '#F4F4F4',
    alignItems: 'center',

  },
  headerStyle: {
    width: 80,
    height: 80,
    borderRadius: 40,
    borderWidth: 1,
    borderColor: 'white',
    marginTop: 50,
    marginBottom: 20
  },
  inputStyle: {
    textAlign: 'center',
    width: width * 0.9,
    height: 50,
    marginTop: 1,
    backgroundColor: 'white',
    //borderRadius: 8,
    alignSelf: 'center',
    color: 'black',
    fontWeight: '600'
  },
  loginBtnStyle: {
    width: width * 0.9,
    height: 50,
    marginTop: 20,
    backgroundColor: '#73B5F9',
    //'borderRadius: 8,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center'
  },
  loginQueStyle: {
    flexDirection: 'row',
    width: width,
    height: 40,
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  loginWayStyle: {
    position: 'absolute',
    flexDirection: 'row',
    height: 50,
    alignItems: 'center',
    bottom: 10,
    left: 10

  },
  loginWayLogoStyle: {
    width: 44,
    height: 44,
    borderRadius: 22,
    marginRight: 8
  }

});

