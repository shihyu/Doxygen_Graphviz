#pragma once

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
					///
					/// <summary> * ������װÿһ������������Ԫ�ض�Ӧ������ </summary>
					/// 
					class ParserModel
					{
					///	
					///	 <summary> * �Ƿ񵥸�ֵ </summary>
					///	 
					private:
						bool singleVlaue;
					///	
					///	 <summary> * �Ƿ����ԣ��������Ծ���Ԫ�� </summary>
					///	 
						bool propertyValue;
					///	
					///	 <summary> * �Ƿ��ս�� </summary>
					///	 
						bool end;
					public:
						virtual bool isEnd();
						virtual void setEnd(bool end);
						virtual bool isSingleVlaue();
						virtual void setSingleVlaue(bool oneVlaue);
						virtual bool isPropertyValue();
						virtual void setPropertyValue(bool propertyValue);
					};

				}
			}
		}
	}
}