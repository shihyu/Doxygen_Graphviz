#pragma once

#include "Visitor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example3
				{
					///
					/// <summary> * �����ʵ�Ԫ�صĽӿ� </summary>
					/// 
					class Element
					{
					///	
					///	 <summary> * ���ܷ����ߵķ��� </summary>
					///	 * <param name="visitor"> �����߶��� </param>
					///	 
					public:
						virtual void accept(Visitor *visitor) = 0;
					};


				}
			}
		}
	}
}