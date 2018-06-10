import React from "react";
import { Root } from "native-base";
import { StackNavigator} from "react-navigation";

import Login from "./screens/login/login";
import MyFooter from "./screens/myfooter/myfooter";



const AppNavigator = StackNavigator(
  {

    Login: {screen: Login},
    MyFooter: {screen: MyFooter},


  },
  {
    initialRouteName: "Login",
    headerMode: "none"
  }
);

export default () =>
  <Root>
    <AppNavigator />
  </Root>;
