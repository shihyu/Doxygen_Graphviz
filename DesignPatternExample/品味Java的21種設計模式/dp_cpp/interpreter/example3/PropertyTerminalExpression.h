#pragma once

#include "ReadXmlExpression.h"
#include "Context.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example3
				{
					///
					/// <summary> * ������Ϊ�ս����Ӧ�Ľ����� </summary>
					/// 
					class PropertyTerminalExpression : public ReadXmlExpression
					{
					///	
					///	 <summary> * ���Ե����� </summary>
					///	 
					private:
						std::string propName;
					public:
						PropertyTerminalExpression(std::string propName);

						virtual std::string *interpret(Context *c);
					};

				}
			}
		}
	}
}