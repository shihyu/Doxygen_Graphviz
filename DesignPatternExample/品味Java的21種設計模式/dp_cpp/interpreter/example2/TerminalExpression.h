#pragma once

#include "AbstractExpression.h"
#include "Context.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example2
				{
					///
					/// <summary> * �ս�����ʽ </summary>
					/// 
					class TerminalExpression : public AbstractExpression
					{

					public:
						virtual void interpret(Context *ctx);
					};
				}
			}
		}
	}
}