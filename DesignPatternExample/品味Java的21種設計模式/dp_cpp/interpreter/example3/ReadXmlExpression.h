#pragma once

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
					/// <summary> * ���ڴ����Զ���Xmlȡֵ���ʽ�Ľӿ� </summary>
					/// 
					class ReadXmlExpression
					{
					///	
					///	 <summary> * ���ͱ��ʽ </summary>
					///	 * <param name="c"> ������ </param>
					///	 * <returns> ���������ֵ��Ϊ��ͨ�ã������ǵ���ֵ��Ҳ�����Ƕ��ֵ��
					///	 *         ��˾ͷ���һ������ </returns>
					///	 
					public:
						virtual std::string *interpret(Context *c) = 0;
					};

				}
			}
		}
	}
}