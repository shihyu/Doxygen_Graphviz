#pragma once

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
					/// <summary> * ������ʽ </summary>
					/// 
					class AbstractExpression
					{
					///	
					///	 <summary> * ���͵Ĳ��� </summary>
					///	 * <param name="ctx"> �����Ķ��� </param>
					///	 
					public:
						virtual void interpret(Context *ctx) = 0;
					};

				}
			}
		}
	}
}