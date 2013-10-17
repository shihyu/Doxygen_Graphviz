#pragma once

#include "ReadXmlExpression.h"
#include "Context.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example5
				{


					using org::w3c::dom::Element;
					///
					/// <summary> * �Զ��Ԫ�ص�������Ϊ�ս���Ľ��ʹ������ </summary>
					/// 
					class PropertysTerminalExpression : public ReadXmlExpression
					{
					///	
					///	 <summary> * �������� </summary>
					///	 
					private:
						std::string propName;
					public:
						PropertysTerminalExpression(std::string propName);

						virtual std::string *interpret(Context *c);
					};

				}
			}
		}
	}
}